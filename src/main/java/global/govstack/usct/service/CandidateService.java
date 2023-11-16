package global.govstack.usct.service;

import global.govstack.usct.controller.dto.CandidateDto;
import global.govstack.usct.controller.dto.CreateCandidateDto;
import global.govstack.usct.controller.dto.CreatePersonDto;
import global.govstack.usct.controller.dto.PackageDto;
import global.govstack.usct.model.Candidate;
import global.govstack.usct.model.Person;
import global.govstack.usct.repositories.CandidateRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CandidateService {
  private final CandidateRepository candidateRepository;
  private final PersonService personService;
  private final PackageService packageService;
  private final ConsentService consentService;

  public CandidateService(
      CandidateRepository candidateRepository,
      PersonService personService,
      PackageService packageService,
      ConsentService consentService) {
    this.candidateRepository = candidateRepository;
    this.personService = personService;
    this.packageService = packageService;
    this.consentService = consentService;
  }

  public List<CandidateDto> findAll() {
    List<Candidate> candidates = candidateRepository.findAll();
    return candidates.stream()
        .map(
            candidate -> {
              List<PackageDto> packageDtoList =
                  candidate.getPackageIds().stream().map(packageService::getById).toList();
              var consent = consentService.findById(candidate.getId());
              return new CandidateDto(candidate, packageDtoList, consent);
            })
        .toList();
  }

  public CandidateDto findById(int id) {
    Candidate candidate =
        candidateRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("Candidate with id: " + id + " doesn't exist"));
    List<PackageDto> packageDtoList =
        candidate.getPackageIds().stream().map(packageService::getById).toList();
    var consent = consentService.findById(candidate.getId());
    return new CandidateDto(candidate, packageDtoList, consent);
  }

  public void deleteById(Integer id) {
    candidateRepository.deleteById(id);
  }

  @Transactional
  public Candidate save(CreateCandidateDto createCandidateDto) {
    CreatePersonDto createPersonDto = createCandidateDto.person();
    Person person = personService.save(createPersonDto);
    Candidate candidate = new Candidate();
    candidate.setPerson(person);
    candidate.setPackageIds(createCandidateDto.packageIds());
    return candidateRepository.save(candidate);
  }

  @Transactional
  public Candidate save(Candidate candidate) {
    personService.save(candidate.getPerson());
    return candidateRepository.save(candidate);
  }
}
