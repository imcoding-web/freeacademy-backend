package fr.imcoding.edu365.business.mappers;

import fr.imcoding.edu365.business.services.UserUtils;
import fr.imcoding.edu365.client.dtos.response.StudentResponse;
import fr.imcoding.edu365.client.dtos.response.UserResponse;
import fr.imcoding.edu365.client.dtos.response.User_Details;
import fr.imcoding.edu365.dtos.ExpertDetails;
import fr.imcoding.edu365.dtos.UserDetails;
import fr.imcoding.edu365.dtos.UserDto;
import fr.imcoding.edu365.persistence.entities.InformationGiver;
import fr.imcoding.edu365.persistence.entities.InformationSeeker;
import fr.imcoding.edu365.persistence.entities.PackageSubscription;
import fr.imcoding.edu365.persistence.entities.SkillSubscription;
import fr.imcoding.edu365.persistence.entities.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

  private final AddressMapper addressMapper;
  private final UserUtils userUtils;
  private final SkillAreaMapper skillAreaMapper;



  public UserDto toUserDto(User user) {
    return new UserDto(user.getUserFirstName(), user.getUserLastName(), user.getCreatedAt());
  }
  public User_Details toUserDetails(User user) {
      if(user == null) return null;
    return User_Details.builder().userUuid(user.getUuid()).userFirstName(user.getUserFirstName()).userLastName(user.getUserLastName()).userProfilePicture(userUtils.getPictureProfile(user)).build();
  }

    public User_Details toTeacherDetails(InformationGiver user) {
        if(user == null) return null;
        return User_Details.builder().userUuid(user.getUuid()).userFirstName(user.getUserFirstName()).userLastName(user.getUserLastName()).userProfilePicture(userUtils.getPictureProfile(user)).currentSchool(user.getCurrentSchool())
                .userDescription(user.getUserDescription()).build();
    }

  public UserDetails toUserDetailsResponse(User user) {
    return new UserDetails(user.getUuid(), user.getUserFirstName(), user.getUserLastName(),
        user.getCreatedAt(),
        user.getUserEmail(), user.getUserPhoneNumber(),
        user.getUserAddress() != null ? addressMapper.toAddressDto(user.getUserAddress()) : null,
        userUtils.getPictureProfile(user)
        //userUtils.getPictureCover(user)

        );
  }

	public ExpertDetails toUserDetailsValidation(InformationGiver user) {
		return ExpertDetails.expertBuilder().userUuid(user.getUuid()).userFirstName(user.getUserFirstName())
				.userLastName(user.getUserLastName()).userEmail(user.getUserEmail())
				.userPhoneNumber(user.getUserPhoneNumber()).userProfilePicture(userUtils.getPictureProfile(user))
				.isValidate(user.isValidate()).build();
	}

  public UserResponse toUserResponse(User user) {
      if(user == null) return null;
    return new UserResponse(user.getUuid(), user.getUserFirstName(), user.getUserLastName(),
        user.getCreatedAt(),
        user.getUserEmail(), user.getUserBirthDate(),
        user.getUserAddress() != null ? addressMapper.toAddressDto(user.getUserAddress()) : null,
        userUtils.getPictureProfile(user), null, null

    );
  }
    public UserResponse toUserResponse(User user, PackageSubscription packageSubscription) {
        if(user == null) return null;
        return new UserResponse(user.getUuid(), user.getUserFirstName(), user.getUserLastName(),
                user.getCreatedAt(),
                user.getUserEmail(), user.getUserBirthDate(),
                user.getUserAddress() != null ? addressMapper.toAddressDto(user.getUserAddress()) : null,
                userUtils.getPictureProfile(user),
                packageSubscription != null,
            packageSubscription != null? packageSubscription.getSkillAreaPackage().getPackageType() : null



        );
    }

  public UserResponse toUserResponse(User user, List<SkillSubscription> skillSubscriptions) {
    if(user == null) return null;
    return new UserResponse(user.getUuid(), user.getUserFirstName(), user.getUserLastName(),
        user.getCreatedAt(),
        user.getUserEmail(), user.getUserBirthDate(),
        user.getUserAddress() != null ? addressMapper.toAddressDto(user.getUserAddress()) : null,
        userUtils.getPictureProfile(user),
        !skillSubscriptions.isEmpty(),
        !skillSubscriptions.isEmpty()? skillSubscriptions.get(0).getPackageType() : null



    );
  }

  public User_Details toUserDetail(InformationGiver user) {
    return User_Details.builder().userUuid(user.getUuid()).userFirstName(user.getUserFirstName()).userLastName(user.getUserLastName()).userProfilePicture(userUtils.getPictureProfile(user)).userDescription(user.getUserDescription()).build();
  }
public StudentResponse toStudentResponse(InformationSeeker user){
    return StudentResponse.builder().userUuid(user.getUuid()).userFirstName(user.getUserFirstName()).userLastName(user.getUserLastName()).userFullName(
        user.getUserFirstName().concat(" ").concat(user.getUserLastName()))
        .skillArea(user.getCurrentLevel()!=null?skillAreaMapper.toSkillAreaDto(user.getCurrentLevel()):null).build();
}
}
