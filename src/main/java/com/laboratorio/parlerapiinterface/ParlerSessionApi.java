package com.laboratorio.parlerapiinterface;

import com.laboratorio.parlerapiinterface.model.ParlerSession;

/**
 *
 * @author Rafael
 * @version 1.1
 * @created 30/09/2024
 * @updated 04/02/2025
 */
public interface ParlerSessionApi {
    String sendMagicLink();
    int checkParlerMagicLinkEmail();
    ParlerSession authenticateUser(int code, String cookieStr);
    ParlerSession authenticateUser();
}