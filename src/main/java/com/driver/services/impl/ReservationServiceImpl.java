package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    UserRepository userRepository3;
    @Autowired
    SpotRepository spotRepository3;
    @Autowired
    ReservationRepository reservationRepository3;
    @Autowired
    ParkingLotRepository parkingLotRepository3;
    @Override
    public Reservation reserveSpot(Integer userId, Integer parkingLotId, Integer timeInHours, Integer numberOfWheels) throws Exception {
        try{
            Optional<User> userOptional = userRepository3.findById(userId) ;
            Optional<ParkingLot> parkingLotOptional = parkingLotRepository3.findById(parkingLotId) ;
            if(!userOptional.isPresent() || !parkingLotOptional.isPresent()){
                throw new Exception ("Cannot make reservation");
            }
            User user = userRepository3.findById(userId).get();
            ParkingLot parkingLot = parkingLotRepository3.findById(parkingLotId).get();
            List<Spot> spotList = parkingLot.getSpotList();
            boolean check = false ;
            for (Spot spot :spotList){
               if(!spot.getOccupied()){
                    check = true ;
                    break;
                }
            }

            if (!check){
                throw new Exception("Cannot make reservation");
            }

            SpotType spotType ;
            if (numberOfWheels>4){
                spotType = SpotType.OTHERS ;
            }
            else if (numberOfWheels>2){
                spotType = SpotType.FOUR_WHEELER  ;
            }
            else{
                spotType = SpotType.TWO_WHEELER ;
            }

            int minPrice = Integer.MAX_VALUE ;
            check = false ;
            Spot spot = null ;

            for (Spot spot1 : spotList){
                if(spotType.equals(SpotType.OTHERS) && spot1.getSpotType().equals(SpotType.OTHERS)) {
                    if (spot1.getPricePerHour() * timeInHours < minPrice && !spot1.getOccupied()) {
                        minPrice = spot1.getPricePerHour() * timeInHours;
                        check = true;
                        spot = spot1;
                    }
                }
                    else if (spotType.equals(SpotType.FOUR_WHEELER) && spot1.getSpotType().equals(SpotType.OTHERS) || spot1.getSpotType().equals(SpotType.FOUR_WHEELER)){
                        if(spot1.getPricePerHour()*timeInHours <minPrice && !spot1.getOccupied()){
                            minPrice = spot1.getPricePerHour()*timeInHours ;
                            spot = spot1 ;
                            check = true ;
                        }
                    }
                else if (spotType.equals(SpotType.TWO_WHEELER) && spot1.getSpotType().equals(SpotType.OTHERS) ||
                        spot1.getSpotType().equals(SpotType.FOUR_WHEELER) || spot1.getSpotType().equals(SpotType.TWO_WHEELER)){
                    if (spot1.getPricePerHour()*timeInHours<minPrice && !spot1.getOccupied()){
                        minPrice = spot1.getPricePerHour() * timeInHours ;
                        spot = spot1 ;
                        check = true ;
                    }
                }
            }
            if (!check){
                throw new Exception("Cannot make reservation");
            }
            assert spot != null ;
            spot.setOccupied(true);

            Reservation reservation = new Reservation() ;
            reservation.setNumberOfHours(timeInHours);
            reservation.setSpot(spot);
            reservation.setUser(user);

            spot.getReservationList().add(reservation);
            spotRepository3.save(spot);
            return reservation ;
        }
        catch(Exception e){
            return null ;
        }
    }
}
