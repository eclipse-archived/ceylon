
package org.eclipse.ceylon.cmr.impl;

/**
 * Thrown when a module artifact has an invalid SHA1 signature.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
@SuppressWarnings("serial")
public class InvalidArchiveException extends RuntimeException {

    private String path;
    private String repository;

    public InvalidArchiveException(String message, String path, String repository) {
        super(message);
        this.path = path;
        this.repository = repository;
    }

    public String getPath(){
        return path;
    }

    public String getRepository() {
        return repository;
    }
}
