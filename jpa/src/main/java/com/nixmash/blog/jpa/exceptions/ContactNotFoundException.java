
    package com.nixmash.blog.jpa.exceptions;

    public class ContactNotFoundException extends Exception {

        private static final long serialVersionUID = -8060531120470573530L;

        private String msg;

        public ContactNotFoundException() {
            super();
        }

        public ContactNotFoundException(String msg) {
            this.msg = System.currentTimeMillis()
                    + ": " + msg;
        }

        public String getMsg() {
            return msg;
        }


    }


