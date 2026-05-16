package com.hardik.problemsservice.model;

import org.springframework.data.annotation.Id;

public record Problem(@Id int id, String title, String acceptance, String difficulty) {}