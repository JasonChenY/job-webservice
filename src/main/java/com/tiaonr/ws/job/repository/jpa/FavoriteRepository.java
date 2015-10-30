package com.tiaonr.ws.job.repository.jpa;

/**
 * Created by echyong on 9/1/15.
 */
import java.util.List;
import com.tiaonr.ws.job.model.Favorite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    // for user to get his own favorite list
    @Query("select u from Favorite u where u.user_id = ?1")
    Page<Favorite> findByUser_id(String user_id, Pageable pageable);

    // for operator to get favorite lists for specific hot job
    @Query("select u from Favorite u where u.job_id = :job_id")
    Page<Favorite> findByJob_id(@Param("job_id") String job_id, Pageable pageable);

    // for checking whether the user already favorited specific job, this check can be done in client side.
    @Query("select count(u) from Favorite u where u.user_id = :user_id and u.job_id = :job_id")
    int count(@Param("job_id")String job_id, @Param("user_id")String user_id);

    // only for get favorite id (if favorited) specific user/job
    @Query("select u from Favorite u where u.user_id = :user_id and u.job_id = :job_id")
    List<Favorite> favorite(@Param("job_id")String job_id, @Param("user_id")String user_id);

    // for get count of favorites for specific job ( display in joblist )
    @Query("select count(u) from Favorite u where u.job_id = ?1")
    int count(String job_id);
}
