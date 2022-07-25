package edu.duke.ece651.mp.common;

public abstract class MoveChecking<T> {
  private final MoveChecking<T> next;
  public String moveStatus;

  public MoveChecking(MoveChecking<T> next) {
    this.next = next;
  }

  protected abstract String checkMyRule(Map<T> map, String source, String destination, int movingunits);

  public String checkMoving(Map<T> map, String source, String destination, int movingunits, String player_color) {
    // update moveStatus
    moveStatus = player_color + ": Move order from " + source + " into " + destination + " with " + movingunits
        + " units was ";

    if (checkMyRule(map, source, destination, movingunits) != null) {
      return checkMyRule(map, source, destination, movingunits);
    }
    if (next != null) {
      return next.checkMoving(map, source, destination, movingunits, player_color);
    }
    return null;
  }
}
