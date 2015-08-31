package com.tiaonaer.ws.job.dto;

import java.util.List;
import java.util.ArrayList;
import com.tiaonaer.ws.job.model.Comment;
import org.springframework.data.domain.Page;

/**
 * Created by echyong on 8/31/15.
 */
public class CommentsDTO extends PageNavigation {
    private List<CommentDTO> comments = new ArrayList<CommentDTO>();
    public CommentsDTO(Page<?> page) {
        super(page);
    }
    public void feedDTOs(List<Comment> models, String user_id) {
        for (Comment comment : models ) {
            CommentDTO dto = new CommentDTO(comment, user_id);
            comments.add(dto);
        }
    }
    public List<CommentDTO> getComments() { return comments; }
}
