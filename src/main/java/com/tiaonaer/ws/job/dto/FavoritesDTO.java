package com.tiaonaer.ws.job.dto;

/**
 * Created by echyong on 9/1/15.
 */
import java.util.List;
import java.util.ArrayList;
import org.springframework.data.domain.Page;
import com.tiaonaer.ws.job.dto.PageNavigation;
import com.tiaonaer.ws.job.dto.FavoriteDTO;

public class FavoritesDTO extends PageNavigation {
    private List<FavoriteDTO> favorites = new ArrayList<FavoriteDTO>();
    public FavoritesDTO(Page<?> page) {
        super(page);
    }
    public void addFavorite(FavoriteDTO dto) { favorites.add(dto);}
    public List<FavoriteDTO> getFavorites() { return favorites; }
}
