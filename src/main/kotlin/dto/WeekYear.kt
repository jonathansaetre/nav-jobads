package dto

data class WeekYear(val week: Int, val year: Int) : Comparable<WeekYear> {
    override fun compareTo(other: WeekYear): Int {
        if (this.year != other.year) {
            return this.year.compareTo(other.year)
        } else {
            return this.week.compareTo(other.week)
        }

    }
}