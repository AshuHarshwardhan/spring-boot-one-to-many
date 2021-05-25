package com.cepheid.cloud.skel.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.cepheid.cloud.skel.model.Description;

public interface DescriptionRepository extends JpaRepository<Description, Long> {

	Page<Description> findByItemId(Long itemId, Pageable pageable);

	Optional<Description> findByIdAndItemId(Long id, Long itemId);
}
