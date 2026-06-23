package fr.imcoding.edu365.business.mappers;

import fr.imcoding.edu365.business.services.PositionService;
import fr.imcoding.edu365.business.services.VerifiedExpertService;
import fr.imcoding.edu365.client.dtos.response.PositionResponse;
import fr.imcoding.edu365.client.dtos.response.ValidationExpertResponse;
import fr.imcoding.edu365.dtos.ExpertDetails;
import fr.imcoding.edu365.dtos.MediaDetails;
import fr.imcoding.edu365.dtos.PositionDto;
import fr.imcoding.edu365.dtos.SpecialityDto;
import fr.imcoding.edu365.enumeration.ValidationStatus;
import fr.imcoding.edu365.persistence.entities.InformationGiver;
import fr.imcoding.edu365.persistence.entities.Position;
import fr.imcoding.edu365.persistence.entities.VerifiedExpert;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author Rokaya
 * @Date 17/07/2022
 */
@Component
@RequiredArgsConstructor
public class VerifiedExpertMapper {
    private final  MediaDatailsMapper mediamapper;
    private final PositionService positionService;

    //private final VerifiedExpertService verifiedExpertService;
    public VerifiedExpert toVerifiedExpert(ExpertDetails expertDetails, PositionResponse positionDetails, ValidationStatus status, InformationGiver user) {
        return new VerifiedExpert(expertDetails.getUserFirstName(),
                expertDetails.getUserLastName(),
                expertDetails.getUserIdentityType(),
                expertDetails.getUserIdentityNumber(),
                expertDetails.getUserIdentityPicture() != null ? expertDetails.getUserIdentityPicture().getMediaUuid() : null,
                positionDetails.getUserPosition() != null ? positionDetails.getUserPosition().getPositionUuid() : null,
                positionDetails.getUserSpeciality() != null ? positionDetails.getUserSpeciality().getSpecialityUuid(): null,
                positionDetails.getLastGraduationYear(),
                positionDetails != null && positionDetails.getCurrentGraduation() != null ? positionDetails.getCurrentGraduation().getDegreeLabel() : null,
                positionDetails != null && positionDetails.getLastGraduation() != null ? positionDetails.getLastGraduation().getDegreeLabel() : null,

                positionDetails.getCustomSpeciality(),
                positionDetails.getCustomPosition(),
                positionDetails.getCvDocument() != null ? positionDetails.getCvDocument().getMediaUuid() : null,
                positionDetails.getCertificateDocument() != null ? positionDetails.getCertificateDocument().getMediaUuid() : null,
                positionDetails.getGraduationDocument() != null ? positionDetails.getGraduationDocument().getMediaUuid() : null,
                expertDetails.getUserProfilePicture() != null ? expertDetails.getUserProfilePicture().getMediaUuid() : null,
                status,
                user,null


        );
    }

    public ValidationExpertResponse toValidationExpertResponse(VerifiedExpert verifiedExpert) {

        System.out.println("verif:::"+verifiedExpert.toString());
        PositionDto position = null;
        SpecialityDto speciality = null;
        if (verifiedExpert.getUserPositionUuid() != null)
            position =positionService.getByUuid(verifiedExpert.getUserPositionUuid());
        if (verifiedExpert.getUser().getUserSpeciality() != null)
            speciality = SpecialityDto.builder().specialityCode(verifiedExpert.getUser().getUserSpeciality().getSpecialityCode())
                    .specialityLabel(verifiedExpert.getUser().getUserSpeciality().getSpecialityLabel()).specialityUuid(verifiedExpert.getUser().getUserSpeciality().getUuid()).build();
        //System.out.println("position:::"+position.toString());

        return ValidationExpertResponse.builder().identifier(verifiedExpert.getUser().getUniqueIdentifier()).validation_uuid(verifiedExpert.getUuid())
                .currentGraduation(verifiedExpert.getCurrentGraduation())
                .lastGraduation(verifiedExpert.getLastGraduation())
                .lastGraduationYear(verifiedExpert.getLastGraduationYear())
                .identityNumber(verifiedExpert.getIdentityNumber()).identityType(verifiedExpert.getIdentityType())
                .userFirstName(verifiedExpert.getUserFirstName()).userlastName(verifiedExpert.getUserLastName())
                .userUuid(verifiedExpert.getUser().getUuid())
                .position(position)
                .speciality(speciality)
                .customPosition(verifiedExpert.getCustomPosition())
                .customSpeciality(verifiedExpert.getCustomSpeciality())
                .certificateDocument( verifiedExpert.getCertificateDocumentUuid()!=null?
                         mediamapper.toMediaDetails(mediamapper.toMedia(MediaDetails.builder().mediaUuid(verifiedExpert.getCertificateDocumentUuid()).build())):null)
                .cvDocument(verifiedExpert.getCvDocumentUuid()!=null?
                        mediamapper.toMediaDetails(mediamapper.toMedia(MediaDetails.builder().mediaUuid(verifiedExpert.getCvDocumentUuid()).build())):null)
                .graduationDocument(verifiedExpert.getGraduationDocumentUuid()!=null?
                        mediamapper.toMediaDetails(mediamapper.toMedia(MediaDetails.builder().mediaUuid(verifiedExpert.getGraduationDocumentUuid()).build())):null)
                .identityDocument(verifiedExpert.getIdentityPictureUuid()!=null?
                        mediamapper.toMediaDetails(mediamapper.toMedia(MediaDetails.builder().mediaUuid(verifiedExpert.getIdentityPictureUuid()).build())):null)
                .profilePicture(verifiedExpert.getPictureProfileUuid()!=null?
                        mediamapper.toMediaDetails(mediamapper.toMedia(MediaDetails.builder().mediaUuid(verifiedExpert.getPictureProfileUuid()).build())):null)
                .build();
    }

    public ValidationExpertResponse toExpertValidationResponse(VerifiedExpert verifiedExpert) {
        PositionDto position = null;
        if (verifiedExpert.getUser().getUserPosition() != null)
          position = PositionDto.builder().positionCode(verifiedExpert.getUser().getUserPosition().getPositionCode())
                .positionUuid(verifiedExpert.getUser().getUserPosition().getUuid())
                .positionLabel(verifiedExpert.getUser().getUserPosition().getPositionLabel()).build();
        return  ValidationExpertResponse.builder().userFirstName(verifiedExpert.getUserFirstName())
            .userlastName(verifiedExpert.getUserLastName())
            .identifier(verifiedExpert.getUser().getUniqueIdentifier())
            .position(position).customPosition(verifiedExpert.getCustomPosition())
            .profilePicture(verifiedExpert.getPictureProfileUuid()!=null?
                mediamapper.toMediaDetails(mediamapper.toMedia(MediaDetails.builder()
                    .mediaUuid(verifiedExpert.getPictureProfileUuid()).build())):null)
            .accountStatus(verifiedExpert.getUser().getAccountStatus())
            //.isValidatedAccount(verifiedExpertService.checkPartialValidation(verifiedExpert.getUser().getUuid()))

            .build();
    }
}
