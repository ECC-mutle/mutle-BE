package com.mutle.mutle.dto;

import java.util.List;

public record ItunesResponse(int resultCount, List<MusicSearchResult> results) {}
