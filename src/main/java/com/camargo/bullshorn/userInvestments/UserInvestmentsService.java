package com.camargo.bullshorn.userInvestments;

import com.camargo.bullshorn.appuser.AppUser;
import com.camargo.bullshorn.appuser.AppUserService;
import com.camargo.bullshorn.marketdata.model.QuoteDataModel;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
@AllArgsConstructor
public class UserInvestmentsService {

    private final UserInvestmentsRepository userInvestmentsRepository;
    private final AppUserService appUserService;

    public String addInvestment(UserInvestments userInvestments) {

        AppUser appUser = appUserService.loadUserByUsername(
                SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        System.out.println(appUser);
        userInvestments.setAppUser(appUser);
        userInvestmentsRepository.save(userInvestments);
        return "Added";
    }

    public List<UserInvestments> getUserInvestments(){
        AppUser appUser = appUserService.loadUserByUsername(
                SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        return userInvestmentsRepository.findAllByAppUser(appUser).orElseThrow(()-> new IllegalStateException("No items to show."));
    }

    public boolean deleteInvestmentItem(Long id){

        AppUser appUser = appUserService.loadUserByUsername(
                SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        UserInvestments item = userInvestmentsRepository.getById(id);
        if(item == null || appUser == null || !item.getAppUser().getEmail().equals(appUser.getEmail())){
            return false;
        }
        System.out.println(id);
        userInvestmentsRepository.deleteById(id);
        return true;
    }
}
