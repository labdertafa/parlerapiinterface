package com.laboratorio.parlerapiinterface;

import com.laboratorio.parlerapiinterface.model.ParlerSession;
import com.laboratorio.parlerapiinterface.model.response.ParlerMagicLinkResponse;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 30/09/2024
 * @updated 30/09/2024
 */
public interface ParlerSessionApi {
    ParlerMagicLinkResponse sendMagicLink();
    int checkParlerMagicLinkEmail();
    ParlerSession authenticateUser(int code);
    ParlerSession authenticateUser();
}