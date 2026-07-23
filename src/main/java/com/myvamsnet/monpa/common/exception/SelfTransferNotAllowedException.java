package com.myvamsnet.monpa.common.exception;

public class SelfTransferNotAllowedException extends RuntimeException {

    public SelfTransferNotAllowedException() {
        super("You cannot transfer money to your own wallet.");
    }
}