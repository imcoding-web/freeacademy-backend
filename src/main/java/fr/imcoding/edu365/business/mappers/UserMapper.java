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
import fr.imcoding.edu365.persistence.entities.User;
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
    return User_Details.builder().userUuid(user.getUuid()).userFirstName(user.getUserFirstName()).userLastName(user.getUserLastName()).userProfilePicture(userUtils.getPictureProfile(user)).build();
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
    return new UserResponse(user.getUuid(), user.getUserFirstName(), user.getUserLastName(),
        user.getCreatedAt(),
        user.getUserEmail(), user.getUserBirthDate(),
        user.getUserAddress() != null ? addressMapper.toAddressDto(user.getUserAddress()) : null,
        userUtils.getPictureProfile(user)


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
