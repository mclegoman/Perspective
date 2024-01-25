/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.common.util;

public record Pair<f, s>(f first, s second) {
	public f getFirst() {
		return first;
	}
	public s getSecond() {
		return second;
	}
}
