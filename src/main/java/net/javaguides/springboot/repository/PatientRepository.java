package net.javaguides.springboot.repository;

import net.javaguides.springboot.model.Patient;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends CrudRepository<Patient, Long>, QueryByExampleExecutor<Patient> {
    
    List<Patient> findByName(String name);
    List<Patient> findByOrderByDateOfAdmissionDesc();
    List<Patient> findByOrderByIdDesc();

//    @Query(value = "SELECT next_val FROM user_sequence", nativeQuery =
//            true)
//    Long getNextDispatchId();

//    @Modifying
//    @Transactional
//    @Query(value = "update user_sequence as t1,(select next_val from user_sequence ) as t2 set t1.next_val = t2.next_val", nativeQuery =
//            true)
//    void incrementDispatchID();

//    @Query(value = "SELECT * FROM Users u WHERE u.status = :status and u.name = :name",
//            nativeQuery = true)
//    User findUserByStatusAndNameNamedParamsNative(
//            @Param("status") Integer status, @Param("name") String name);

//    @Query(value = "SELECT * FROM patient p where p.id = :id ", nativeQuery = true)
//    List<Patient> getSearchResult(String id);
    
}
