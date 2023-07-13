
package model;
/**
 *
 * @author bruno
 */
public class Funcionario {
    private double salary;
    private int dependents;
    private double insalubrity;
    private double overtime;
    private double bonus;
    private double nightShift;

    public Funcionario(){
    }
    
    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public int getDependents() {
        return dependents;
    }

    public void setDependents(int dependents) {
        this.dependents = dependents;
    }

    public double getInsalubrity() {
        return insalubrity;
    }

    public void setInsalubrity(double insalubrity) {
        this.insalubrity = insalubrity;
    }

    public double getOvertime() {
        return overtime;
    }

    public void setOvertime(double overtime) {
        this.overtime = overtime;
    }

    public double getBonus() {
        return bonus;
    }

    public void setBonus(double bonus) {
        this.bonus = bonus;
    }

    public double getNightShift() {
        return nightShift;
    }

    public void setNightShift(double nightShift) {
        this.nightShift = nightShift;
    }
}
