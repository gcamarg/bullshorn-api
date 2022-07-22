package com.camargo.bullshorn.userInvestments;

import com.camargo.bullshorn.appuser.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserInvestmentsRepository extends JpaRepository<UserInvestments, Long> {

    Optional<List<UserInvestments>> findAllByAppUser(AppUser appUser);
}
