package global.govstack.usct.service;

import global.govstack.usct.controller.dto.ConsentDto;
import global.govstack.usct.model.Consent;
import global.govstack.usct.repositories.ConsentRepository;
import global.govstack.usct.types.ConsentStatus;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class ConsentService {

  private final ConsentRepository consentRepository;

  public ConsentService(ConsentRepository consentRepository) {
    this.consentRepository = consentRepository;
  }

  public ConsentDto findById(int consentId) {
    Optional<Consent> consent = consentRepository.findById(consentId);
    return consent
        .map(ConsentDto::new)
        .orElseGet(() -> new ConsentDto(null, ConsentStatus.NOT_GRANTED, null));
  }

  public Consent save(Consent consent) {
    return consentRepository.save(consent);
  }
}
