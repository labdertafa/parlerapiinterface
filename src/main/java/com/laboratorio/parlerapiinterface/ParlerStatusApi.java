package com.laboratorio.parlerapiinterface;

import com.laboratorio.parlerapiinterface.model.ParlerStatus;
import com.laboratorio.parlerapiinterface.model.response.ParlerImageUploadResponse;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 01/10/2024
 * @updated 06/10/2024
 */
public interface ParlerStatusApi {
    ParlerStatus postStatus(String text);
    ParlerStatus postStatus(String text, String imagePath);
    ParlerStatus postStatusWithImage(String text, ParlerImageUploadResponse image);
    ParlerImageUploadResponse uploadImage(String filePath);
    boolean deleteStatus(String id);
}