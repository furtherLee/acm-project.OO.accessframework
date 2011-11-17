/*
 * Copyright (C) 2007 ETH Zurich
 *
 * This file is part of Fosstrak (www.fosstrak.org).
 *
 * Fosstrak is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1, as published by the Free Software Foundation.
 *
 * Fosstrak is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Fosstrak; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA  02110-1301  USA
 */

package org.acm_project.acm09.OO.epcis.accessframework.demoTestCapture;

/**
 * This Exception indicates that the CaptureClient encountered a problem while
 * trying to send a request to the EPCIS capture interface.
 * 
 * @author Marco Steybe
 */
public class CaptureBrokerException extends Exception {

    private static final long serialVersionUID = 4034170925462066270L;

    public CaptureBrokerException() {
        super();
    }

    public CaptureBrokerException(String message, Throwable cause) {
        super(message, cause);
    }

    public CaptureBrokerException(String message) {
        super(message);
    }

    public CaptureBrokerException(Throwable cause) {
        super(cause);
    }
}
