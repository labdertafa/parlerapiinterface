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
 * @updated 12/10/2024
 */
public interface ParlerStatusApi {
    ParlerStatus postStatus(String text);
    ParlerStatus postStatus(String text, String imagePath);
    ParlerStatus postStatusWithImage(String text, ParlerImageUploadResponse image);
    ParlerImageUploadResponse uploadImage(String filePath);
    boolean deleteStatus(String id);
    
    List<ParlerStatusHeader> getGlobalTimeLineHeaders(int quantity);
    List<ParlerStatus> getStatusDetails(List<String> statusIds);
    List<ParlerStatus> getGlobalTimeline(int quantity);
}