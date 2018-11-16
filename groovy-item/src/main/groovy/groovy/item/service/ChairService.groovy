package groovy.item.service

import grails.gorm.services.Service
import groovy.item.domain.Chair

import javax.validation.Valid
import javax.validation.constraints.NotNull

@Service(Chair)
interface ChairService {
    Chair save(@Valid Chair chair)
    List<Chair> findAll()
    int count()
    Chair find(@NotNull Long id)
}