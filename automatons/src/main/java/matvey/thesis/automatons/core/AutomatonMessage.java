// $Id: AutomatonMessage.java,v 1.1 2006/02/18 07:00:34 matvey Exp $
/**
 * Date: 27.05.2005
 * Copyright (c) Matvey Kazakov 2004.
 */
package matvey.thesis.automatons.core;

/**
 * This is special automaton event with argument.
 * @author Matvey Kazakov
 */
public class AutomatonMessage<T> extends AutomatonEvent {
    private T object;

    public AutomatonMessage(long source, long dest, long event, T object) {
        super(source, dest, event);
        this.object = object;
    }

    public AutomatonMessage(long source, long dest, long event) {
        this(source, dest, event, null);
    }

    public T getObject() {
        return object;
    }

    public String toString() {
        return super.toString() + ": " + String.valueOf(object);
    }

}

/*
 * $Log: AutomatonMessage.java,v $
 * Revision 1.1  2006/02/18 07:00:34  matvey
 * just added
 *
 */