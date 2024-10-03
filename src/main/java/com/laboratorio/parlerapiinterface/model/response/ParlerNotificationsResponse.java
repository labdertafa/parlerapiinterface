package com.laboratorio.parlerapiinterface.model.response;

import com.laboratorio.parlerapiinterface.model.ParlerNotification;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 02/10/2024
 * @updated 02/10/2024
 */

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ParlerNotificationsResponse {
    private List<ParlerNotification> data;
    private String path;
    private int per_page;
    private String next_cursor;
    private String next_page_url;
    private String prev_cursor;
    private String prev_page_url;
}