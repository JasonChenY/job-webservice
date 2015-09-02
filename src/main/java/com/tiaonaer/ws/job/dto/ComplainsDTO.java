package com.tiaonaer.ws.job.dto;

import java.util.List;
import java.util.ArrayList;
import com.tiaonaer.ws.job.model.Complain;
import org.springframework.data.domain.Page;

/**
 * Created by echyong on 9/2/15.
 */
public class ComplainsDTO extends PageNavigation {
    private List<ComplainDTO> complains = new ArrayList<ComplainDTO>();
    public ComplainsDTO(Page<?> page) {
        super(page);
    }
    public void feedDTOs(List<Complain> models) {
        for (Complain complain : models ) {
            ComplainDTO dto = new ComplainDTO(complain);
            complains.add(dto);
        }
    }
    public void addComplain(ComplainDTO dto) { complains.add(dto);}
    public List<ComplainDTO> getComplains() { return complains; }
}