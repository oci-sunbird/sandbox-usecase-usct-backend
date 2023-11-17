package global.govstack.usct.service;

import global.govstack.usct.model.Consent;
import global.govstack.usct.repositories.ConsentRepository;
import org.springframework.stereotype.Service;

@Service
public class ConsentService {

  private final ConsentRepository consentRepository;

  public ConsentService(ConsentRepository consentRepository) {
    this.consentRepository = consentRepository;
  }

  public Consent findById(int consentId) {
    return consentRepository
        .findById(consentId)
        .orElseThrow(
            () -> new RuntimeException("Consent with id: " + consentId + " doesn't exist"));
  }

  public Consent save(Consent consent) {
    return consentRepository.save(consent);
  }
}
