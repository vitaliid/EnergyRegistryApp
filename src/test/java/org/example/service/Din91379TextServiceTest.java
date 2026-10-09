package org.example.service;

import org.example.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.text.Normalizer;

import static org.junit.jupiter.api.Assertions.*;

class Din91379TextServiceTest {

    // Both strings render as "é", but they differ in code points.
    private static final String E_DECOMPOSED = "é";   // 2 code points: e + combining acute
    private static final String E_COMPOSED = "é";      // 1 code point: é (NFC)

    private Din91379TextService service;

    @BeforeEach
    void setUp() {
        service = new Din91379TextService();
        service.init();
    }

    // Escapes are used on purpose: editors/IDEs may silently re-normalize literal characters.
    @ParameterizedTest(name = "{2}")
    @CsvSource({
            "é, é, e + acute -> e-acute",
            "ü, ü, u + diaeresis -> u-umlaut",
            "ö, ö, o + diaeresis -> o-umlaut",
            "ä, ä, a + diaeresis -> a-umlaut",
            "Ö, Ö, O + diaeresis -> O-umlaut",
            "ñ, ñ, n + tilde -> n-tilde",
            "ç, ç, c + cedilla -> c-cedilla",
            "å, å, a + ring -> a-ring",
            "š, š, s + caron -> s-caron",
            "ž, ž, z + caron -> z-caron"
    })
    void normalizesDecomposedToComposed(String decomposed, String composed, String description) {
        // guards the test data itself: the input must be NFD, not NFC
        assertFalse(Normalizer.isNormalized(decomposed, Normalizer.Form.NFC));
        assertNotEquals(composed, decomposed);

        String normalizedResult = service.normalize(decomposed);

        assertTrue(Normalizer.isNormalized(normalizedResult, Normalizer.Form.NFC));
        assertEquals(1, normalizedResult.codePointCount(0, normalizedResult.length()));
        assertEquals(composed, normalizedResult);
    }

    @Test
    void normalizeKeepsNull() {
        assertNull(service.normalize(null));
    }

    @Test
    void normalizesAlreadyComposedTextUnchanged() {
        assertEquals(E_COMPOSED, service.normalize(E_COMPOSED));
    }

    @Test
    void normalizeAndValidateReturnsNormalizedText() {
        String input = "Caf" + E_DECOMPOSED + " Müller";   // NFD: e+◌́ and u+◌̈
        assertFalse(Normalizer.isNormalized(input, Normalizer.Form.NFC));

        String result = service.normalizeAndValidate(input);

        assertEquals("Café Müller", result);
        assertTrue(Normalizer.isNormalized(result, Normalizer.Form.NFC));
    }

    @Test
    void acceptsWhitespaceAndAscii() {
        assertEquals("a & b <c>\n\t1", service.normalizeAndValidate("a & b <c>\n\t1"));
    }

    @Test
    void rejectsGreekAndCyrillic() {
        assertThrows(BusinessException.class, () -> service.normalizeAndValidate("Ω"));
        assertThrows(BusinessException.class, () -> service.normalizeAndValidate("Ж"));
    }

    @Test
    void rejectsControlCharacters() {
        assertThrows(BusinessException.class, () -> service.normalizeAndValidate("a\u0001b"));
    }
}
