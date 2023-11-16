package global.govstack.usct.controller;

import global.govstack.usct.controller.dto.ConsentDto;
import global.govstack.usct.model.Consent;
import global.govstack.usct.service.ConsentService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin
@PreAuthorize("hasAnyRole('REGISTRY_OFFICER','ENROLLMENT_OFFICER')")
public class ConsentController {

  private final ConsentService consentService;
  private final ModelMapper modelMapper;

  public ConsentController(ConsentService consentService, ModelMapper modelMapper) {
    this.consentService = consentService;
    this.modelMapper = modelMapper;
  }

  @PutMapping("/consent/{id}")
  @ResponseStatus(HttpStatus.OK)
  ConsentDto updateCandidate(@RequestBody ConsentDto consentDto) {
    if (consentDto.getId() == 0) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid id");
    }
    var consent = convertToEntity(consentDto);
    var updatedCandidate = consentService.save(consent);
    return convertToDto(updatedCandidate);
  }

  private Consent convertToEntity(ConsentDto consentDto) {
    return modelMapper.map(consentDto, Consent.class);
  }

  private ConsentDto convertToDto(Consent consent) {
    return modelMapper.map(consent, ConsentDto.class);
  }
}
