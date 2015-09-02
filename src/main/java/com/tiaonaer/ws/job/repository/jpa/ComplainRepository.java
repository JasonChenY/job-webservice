package com.tiaonaer.ws.job.repository.jpa;

import com.tiaonaer.ws.job.model.Complain;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by echyong on 9/2/15.
 */
public interface ComplainRepository extends JpaRepository<Complain, Long> {
    // for user to check status about his/her own complains
    // also for operator to check complains history of specific user
    @Query("select u from Complain u where u.user_id = ?1")
    Page<Complain> findByUser_id(String user_id, Pageable pageable);

    // for operator to check all complains via status
    @Query("select u from Complain u where u.type = :type and u.status = :status")
    Page<Complain> findByTypeStatus(@Param("type")int type, @Param("status")int status, Pageable pageable);

    // for checking whether the user already complain about specific job, this check can be done in client side.
    @Query("select count(u) from Complain u where u.user_id = :user_id and u.job_id = :job_id")
    int count(@Param("job_id")String job_id, @Param("user_id")String user_id);
}
