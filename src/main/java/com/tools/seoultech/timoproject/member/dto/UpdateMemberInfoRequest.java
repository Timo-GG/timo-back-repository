package com.tools.seoultech.timoproject.member.dto;


public record UpdateMemberInfoRequest(String username, String puuid, String playerName, String playerTag, String profileIconUrl,
                                      String univName, String univEmail) {
}
