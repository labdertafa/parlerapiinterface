package com.laboratorio.parlerapiinterface;

import com.laboratorio.parlerapiinterface.model.ParlerStatus;
import com.laboratorio.parlerapiinterface.model.ParlerStatusHeader;
import com.laboratorio.parlerapiinterface.model.response.ParlerImageUploadResponse;
import java.util.List;

/**
 *
 * @author Rafael
 * @version 1.1
 * @created 01/10/2024
 * @updated 07/10/2024
 */
public interface ParlerStatusApi {
    ParlerStatus postStatus(String text);
    ParlerStatus postStatus(String text, String imagePath);
    ParlerStatus postStatusWithImage(String text, ParlerImageUploadResponse image);
    ParlerImageUploadResponse uploadImage(String filePath);
    boolean deleteStatus(String id);
    
    List<ParlerStatusHeader> getGlobalTimeLineHeaders(int quantity);
    List<ParlerStatus> getGlobalTimeline(List<String> statusIds);
}