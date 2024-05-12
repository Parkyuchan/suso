package ldpd.suso.repository;

import ldpd.suso.entity.Sign;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SignRepository extends JpaRepository<Sign, Integer> {

    Page<Sign> findByTitleContaining(String searchKeyword, Pageable pageable);

    Sign findByTitle(String title);
}
