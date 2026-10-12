package net.kjentytek303.untransfur.util;

import org.jetbrains.annotations.Nullable;

import java.util.Objects;


public class NullablePair<First_t, Second_t> {
	public @Nullable First_t first;
	public @Nullable Second_t second;

	public NullablePair(@Nullable First_t first, @Nullable Second_t second) {
		this.first = first;
		this.second = second;
	}

	@Override
	public boolean equals(@Nullable Object other) {
		if (other == this) {
			return true;
		}
		if(other instanceof NullablePair<?, ?> other1) {
			return (Objects.equals(first, other1.first)) &&
				(Objects.equals(second, other1.second))
			;
		}

		return false;
	}
}