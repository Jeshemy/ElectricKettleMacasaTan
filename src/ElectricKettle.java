public class ElectricKettle {

    private int capacity;
    int temperature;
    private int maxTemperature;
    private int waterLevel;
    private boolean isOn;


    public ElectricKettle() {
        capacity = 1700;
        temperature = 25;
        maxTemperature = 100;
        waterLevel = 0;
        isOn = false;
        System.out.println("Electric Kettle created");
    }

    public int getCapacity() {
        return capacity;
    }

    public int getTemperature() {
        return temperature;
    }

    public int getLevel() {
        return waterLevel;
    }

    public boolean isOn() {
        return isOn;
    }

    public void turnOn() {
        if (!isEmpty()) {
            isOn = true;
        }
    }

    public void turnOff() {
        isOn = false;
    }

    public void heatWater() {
        if (isOn && waterLevel > 0 && temperature < maxTemperature) {
            temperature += 5;
            if (temperature >= maxTemperature) {
                temperature = maxTemperature;
                isOn = false;
            }
        }
    }

    public boolean isBoiled() {
        return temperature >= maxTemperature;
    }

    public void reset() {
        temperature = 25;
        waterLevel = 0;
        isOn = false;
    }

    public void addWater(int amount) {
        if (amount > 0) {
            waterLevel += amount;
            if (waterLevel > capacity) {
                waterLevel = capacity;
            }
        }
    }

    public void removeWater(int amount) {
        if (amount > 0) {
            waterLevel -= amount;
            if (waterLevel < 0) {
                waterLevel = 0;
            }
        }
    }

    public boolean isFull() {
        return waterLevel >= capacity;
    }

    public boolean isEmpty() {
        return waterLevel <= 0;
    }

    public String getPowerStatus() {
        return isOn ? "ON" : "OFF";
    }

    public boolean isHeating() {
        return isOn && !isBoiled() && waterLevel > 0;
    }

}
