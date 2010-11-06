// $Id: ASObjectNotFoundException.java,v 1.1 2006/02/18 07:00:33 matvey Exp $
/**
 * Date: 09.05.2005
 * Copyright (c) Matvey Kazakov 2004.
 */
package matvey.thesis.automatons.core;

/**
 * @author Matvey Kazakov
 */
public class ASObjectNotFoundException extends RuntimeException {
    public ASObjectNotFoundException() {
    }

    public ASObjectNotFoundException(String message) {
        super(message);
    }

    public ASObjectNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ASObjectNotFoundException(Throwable cause) {
        super(cause);
    }
}

/*
 * $Log: ASObjectNotFoundException.java,v $
 * Revision 1.1  2006/02/18 07:00:33  matvey
 * just added
 *
 */