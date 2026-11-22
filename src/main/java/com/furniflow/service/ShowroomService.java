package com.furniflow.service;

import com.furniflow.entity.Showroom;
import java.util.List;

public interface ShowroomService {

    Showroom createShowroom(Showroom showroom);

    List<Showroom> getAllShowrooms();

    Showroom getShowroomById(Long id);

    Showroom updateShowroom(Long id, Showroom showroom);

    void deleteShowroom(Long id);
}