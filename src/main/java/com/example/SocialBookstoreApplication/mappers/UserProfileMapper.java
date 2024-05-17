package com.example.SocialBookstoreApplication.mappers;
import com.example.SocialBookstoreApplication.domainmodel.UserProfile;
import com.example.SocialBookstoreApplication.formsdata.UserProfileFormData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.jpa.repository.JpaRepository;
@Mapper(componentModel = "spring")
public interface UserProfileMapper extends JpaRepository<UserProfile, Integer> {
    UserProfile findByUsername(String username);

    @Mapping(source = "username", target = "username")
    @Mapping(source = "fullName", target = "fullName")
    @Mapping(source = "age", target = "age")
    UserProfileFormData toUserProfileFormData(UserProfile userProfile);

    @Mapping(source = "username", target = "username")
    @Mapping(source = "fullName", target = "fullName")
    @Mapping(source = "age", target = "age")
    UserProfile toUserProfile(UserProfileFormData userProfileFormData);
}
