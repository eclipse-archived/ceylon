

package org.eclipse.ceylon.cmr.impl;

import java.io.File;

import org.eclipse.ceylon.cmr.api.CmrRepository;
import org.eclipse.ceylon.cmr.api.RepositoryBuilder;

/**
 * Repository builder for JDKRepository
 *
 * @author Tako Schotanus (tako@ceylon-lang.org)
 */
public class JDKRepositoryBuilder implements RepositoryBuilder {

    @Override
    public String absolute(File cwd, String token) {
        if (token.equals("jdk") || token.equals("jdk:") || token.equals("jdk:/#")) {
            return token;
        } else {
            return null;
        }
    }

    @Override
    public CmrRepository[] buildRepository(String token) throws Exception {
        return buildRepository(token, EMPTY_CONFIG);
    }

    @Override
    public CmrRepository[] buildRepository(String token, RepositoryBuilderConfig config) throws Exception {
        if (token.equals("jdk") || token.equals("jdk:") || token.equals("jdk:/#")) {
            return new CmrRepository[] { new JDKRepository() };
        } else {
            return null;
        }
    }
}
