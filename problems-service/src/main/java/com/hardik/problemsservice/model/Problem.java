package com.hardik.problemsservice.model;

public record Problem( @Id int id, String title, String acceptance, String difficulty){}
