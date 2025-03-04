package com.laboratorio.parlerapiinterface;

import com.laboratorio.parlerapiinterface.model.ParlerSession;

/**
 *
 * @author Rafael
 * @version 1.2
 * @created 30/09/2024
 * @updated 04/03/2025
 */
public interface ParlerSessionApi {
    ParlerSession createSession();
    String sendMagicLink();
    int checkParlerMagicLinkEmail();
    ParlerSession authenticateUser(int code, String cookieStr);
    ParlerSession authenticateUser();
}