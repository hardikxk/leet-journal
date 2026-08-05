package com.hardik.mailservice.dto;

public record EmailRequest(String to, String subject, String text) {
}
