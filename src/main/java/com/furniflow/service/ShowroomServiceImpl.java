package com.furniflow.service;

import com.furniflow.entity.Showroom;
import com.furniflow.exception.ResourceNotFoundException;
import com.furniflow.repository.ShowroomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShowroomServiceImpl implements ShowroomService {

    private final ShowroomRepository showroomRepository;

    public ShowroomServiceImpl(ShowroomRepository showroomRepository) {
        this.showroomRepository = showroomRepository;
    }

    @Override
    public Showroom createShowroom(Showroom showroom) {

        if (showroom.getShowroomName() == null ||
                showroom.getShowroomName().trim().isEmpty()) {
            throw new RuntimeException("Showroom name is required");
        }

        if (showroom.getStatus() == null ||
                showroom.getStatus().trim().isEmpty()) {
            showroom.setStatus("ACTIVE");
        }

        return showroomRepository.save(showroom);
    }

    @Override
    public List<Showroom> getAllShowrooms() {
        return showroomRepository.findAll();
    }

    @Override
    public Showroom getShowroomById(Long id) {

        return showroomRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Showroom not found"));
    }

    @Override
    public Showroom updateShowroom(Long id, Showroom showroom) {

        Showroom existing = showroomRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Showroom not found"));

        if (showroom.getShowroomName() != null) {
            existing.setShowroomName(showroom.getShowroomName());
        }

        if (showroom.getLocation() != null) {
            existing.setLocation(showroom.getLocation());
        }

        if (showroom.getPhone() != null) {
            existing.setPhone(showroom.getPhone());
        }

        if (showroom.getStatus() != null) {
            existing.setStatus(showroom.getStatus());
        }

        return showroomRepository.save(existing);
    }

    @Override
    public void deleteShowroom(Long id) {

        Showroom existing = showroomRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Showroom not found"));

        showroomRepository.delete(existing);
    }
}