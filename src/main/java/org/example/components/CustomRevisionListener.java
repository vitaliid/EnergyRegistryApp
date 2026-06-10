package org.example.components;

import org.example.domain.CustomRevisionEntity;
import org.hibernate.envers.RevisionListener;

import java.util.UUID;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;

public class CustomRevisionListener implements RevisionListener {

    @Override
    public void newRevision(Object revisionEntity) {

        CustomRevisionEntity revision =
                (CustomRevisionEntity) revisionEntity;

        /* Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {
            revision.setUsername(auth.getName());
        } else {
            revision.setUsername("SYSTEM");
        }
        */
        revision.setUsername("Test Dyna user");

        revision.setApplication("dena-app");

        //revision.setRequestId(RequestContext.getRequestId());
        revision.setRequestId(UUID.randomUUID().toString());

        //revision.setRemoteAddress(RequestContext.getRemoteAddress());
        revision.setRemoteAddress("some address");
    }
}