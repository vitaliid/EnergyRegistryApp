package org.example.service;

import jakarta.annotation.PostConstruct;
import org.example.exception.BusinessException;
import org.example.exception.ErrorCode;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.IOException;
import java.io.StringReader;
import java.io.UncheckedIOException;
import java.text.Normalizer;

/**
 * Normalizes texts to Unicode Normalization Form C and then validates them against
 * DIN 91379 datatype C (din-norm-91379-datatypes.xsd). Normalization always happens first,
 * so that e.g. "e" + U+0301 is accepted as the precomposed "é".
 */
@Service
public class Din91379TextService {

    private static final String SCHEMA_LOCATION = "xsd/din91379-datatypeC-wrapper.xsd";
    private static final String ROOT_NAMESPACE = "urn:example:din91379-validation";

    private Schema schema;

    @PostConstruct
    void init() {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            factory.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "jar,file");
            schema = factory.newSchema(new StreamSource(new ClassPathResource(SCHEMA_LOCATION).getURL().toExternalForm()));
        } catch (SAXException e) {
            throw new IllegalStateException("Cannot load DIN 91379 schema", e);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /** Returns the NFC form of the text; null stays null. */
    public String normalize(String text) {
        return text == null ? null : Normalizer.normalize(text, Normalizer.Form.NFC);
    }

    /** True if the text, as is (without normalizing), conforms to datatype C. */
    public boolean isValid(String text) {
        if (text == null) {
            return true;
        }
        String xml = "<text xmlns=\"" + ROOT_NAMESPACE + "\">" + escape(text) + "</text>";
        try {
            schema.newValidator().validate(new StreamSource(new StringReader(xml)));
            return true;
        } catch (SAXException e) {
            // also covers characters that are not even legal in XML (e.g. control characters)
            return false;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Normalizes to NFC, then validates against datatype C.
     *
     * @return the normalized text
     * @throws BusinessException INVALID_FIELD_FORMAT if the normalized text contains characters outside datatype C
     */
    public String normalizeAndValidate(String text) {
        String normalized = normalize(text);
        if (!isValid(normalized)) {
            throw new BusinessException(ErrorCode.INVALID_FIELD_FORMAT);
        }
        return normalized;
    }

    private static String escape(String text) {
        return text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}
