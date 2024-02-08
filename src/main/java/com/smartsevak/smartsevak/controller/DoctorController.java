package com.smartsevak.smartsevak.controller;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

@RestController
@RequestMapping
public class DoctorController {
//    @Autowired
//    ObjectMapper objectMapper;
//
//    @Value("classpath:data/Availibility.json")
//    Resource resourceFile;

    public DoctorController() throws IOException {
    }

    String getDay(DayOfWeek day){
        String dayOfWeek="";
        if(day==DayOfWeek.MONDAY) {
             dayOfWeek="monday";
        }
        else if(day==DayOfWeek.TUESDAY) {
            dayOfWeek="tuesday";
        }
        else if(day==DayOfWeek.WEDNESDAY) {
             dayOfWeek="wednesday";
        }
        else if(day==DayOfWeek.THURSDAY) {
            dayOfWeek="thursday";
        }
        else if(day==DayOfWeek.FRIDAY) {
            dayOfWeek="friday";
        }else if(day==DayOfWeek.SATURDAY) {
             dayOfWeek="saturday";
        }else if(day==DayOfWeek.SUNDAY) {
            dayOfWeek="sunday";
        }
        return  dayOfWeek;

    }
    public String getTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return formatter.toString();
    }

    @GetMapping("/GET/doctor-availability/{date}/{time}")
    String home(@PathVariable String date,@PathVariable String time,@RequestBody JsonNode jsonNode) throws IOException, ParseException, JSONException {
//        String date="2024-01-25";
//        String time="14:25";
       String dateTime=date+" "+time;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime0 = LocalDateTime.parse(dateTime, formatter);
        System.out.println(dateTime0);
//        LocalDateTime dateTime2 =dateTime0.plusDays(1);
//        System.out.println(dateTime2);

        DayOfWeek d= dateTime0.getDayOfWeek();
//       System.out.println(d);
       String dString=getDay(d);
        System.out.println(dString);

//        JsonNode jsonNode = objectMapper.readTree(resourceFile.getFile());

        ArrayList<LocalDateTime> dateArrayList=new ArrayList<>();

for (long i=0;i<7;i++){
       try {
           for (JsonNode node : jsonNode.get("availabilityTimings").get(getDay(dateTime0.plusDays(i).getDayOfWeek()))) {
               String start = node.get("start").asText();
               String end = node.get("end").asText();
               LocalDateTime dateTime1 = LocalDateTime.parse((((dateTime0.plusDays(i).toString()).substring(0, 10)) + " " + start), formatter);
               System.out.println(dateTime1);
               LocalDateTime dateTime2 = LocalDateTime.parse(((dateTime0.plusDays(i).toString()).substring(0, 10) + " " + end), formatter);
               System.out.println(dateTime2);
               if ((dateTime2.isBefore(dateTime0)) || (dateTime2.isEqual(dateTime0))) {
               } else {
                   dateArrayList.add(dateTime1);
                   dateArrayList.add(dateTime2);
               }
           }}
       catch(Exception e){
           }}

        LocalDateTime slot= dateTime0;
        boolean isAvailable=false;
        Collections.sort(dateArrayList);
        System.out.println(dateArrayList);
        if(((dateArrayList.get(1).isAfter(dateTime0))&& (dateArrayList.get(0).isBefore(dateTime0)))|| (dateArrayList.get(0).isEqual(dateTime0)) ){
         isAvailable=true;
            JSONObject json = new JSONObject();
            json.put("isAvailable", isAvailable);
            return json.toString();
        }
        else {
        for(int i=0;i<(dateArrayList.size()/2);i=i+2){

        boolean isAfter=dateArrayList.get(i).isAfter(dateTime0);
        if(isAfter){slot=dateArrayList.get(i);break; }
        }
        String s=slot.toString();
            JSONObject json = new JSONObject();
            json.put("isAvailable", isAvailable);
            json.put("nextAvailableSlot",slot);

            return json.toString();
        }
    }
}





