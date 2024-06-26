package global.govstack.usct.service;

import global.govstack.usct.configuration.PaymentProperties;
import global.govstack.usct.controller.dto.BeneficiaryDto;
import global.govstack.usct.model.Beneficiary;
import global.govstack.usct.model.Candidate;
import global.govstack.usct.repositories.BeneficiaryRepository;
import global.govstack.usct.types.PaymentStatus;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BeneficiaryService {
  private final BeneficiaryRepository repository;
  private final CandidateService candidateService;
  private final PaymentProperties properties;
  private final PackageService packageService;

  public BeneficiaryService(
      BeneficiaryRepository repository,
      CandidateService candidateService,
      PaymentProperties properties,
      PackageService packageService) {
    this.repository = repository;
    this.candidateService = candidateService;
    this.properties = properties;
    this.packageService = packageService;
  }

  public List<BeneficiaryDto> findAll() {
    List<Beneficiary> beneficiaries = repository.findAll();
    return beneficiaries.stream()
        .map(
            beneficiary -> {
              return new BeneficiaryDto(
                  beneficiary, packageService.getById(beneficiary.getEnrolledPackageId()));
            })
        .toList();
  }

  public BeneficiaryDto findById(int id) {
    var beneficiary =
        repository
            .findById(id)
            .orElseThrow(
                () -> new RuntimeException("Beneficiary with id: " + id + " doesn't exist"));
    return new BeneficiaryDto(
        beneficiary, packageService.getById(beneficiary.getEnrolledPackageId()));
  }

  public Beneficiary create(Candidate candidate, int enrolledPackageId) {
    log.info("Create beneficiary, firstName: {}", candidate.getPerson().getFirstName());
    String functionalId =
        candidate.getPerson().getPersonalIdCode()
            + properties.governmentIdentifier()
            + enrolledPackageId;

    Beneficiary beneficiary = new Beneficiary();
    beneficiary.setPerson(candidate.getPerson());
    beneficiary.setEnrolledPackageId(enrolledPackageId);
    beneficiary.setPaymentStatus(PaymentStatus.INITIATE);
    beneficiary.setFunctionalId(functionalId);
    Beneficiary savedBeneficiary = repository.save(beneficiary);
    candidate.setIsBeneficiary(true);
    candidateService.save(candidate);

    return savedBeneficiary;
  }
}
