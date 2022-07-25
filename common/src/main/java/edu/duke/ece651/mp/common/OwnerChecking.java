package edu.duke.ece651.mp.common;

public class OwnerChecking<T> extends MoveChecking<T>{

  public OwnerChecking(MoveChecking<T> next){
    super(next);
  };

  @Override
  public String checkMyRule(Map<T> map,String source,String destination,int movingunit){
    Territory<T> s=map.getAllTerritories().get(source);
    Territory<T> d=map.getAllTerritories().get(destination);
    if(!s.getColor().equals(d.getColor())){
      return "not same owner";
    }else if(s.getUnit()<movingunit){
      return "Insuffcient units";
    }else{
       return null;
    }
  }

}
