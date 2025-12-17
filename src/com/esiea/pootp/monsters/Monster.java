package com.esiea.pootp.monsters;

import com.esiea.pootp.attacks.Attack;
import com.esiea.pootp.attacks.DefaultAttack;
import com.esiea.pootp.types.*;

import java.util.ArrayList;


public abstract class Monster {

    protected String name;
    protected int hp;
    protected int speed;
    protected int attack;
    protected int defense;
    protected Type type;
    protected ArrayList<Attack> attacks;
    protected int maxHp;
    protected MonsterState state = MonsterState.NORMAL;
    protected int turnsParalyzed = 0;

    public static boolean isFlooded = false;
    public static int turnsFloodedLeft = 0;
    protected static WaterMonster floodSource = null;
    protected String imagePath;

    public Monster(String name, int hp, int speed, int attack, int defense, Type type, ArrayList<Attack> attacks, String imagePath) {
        this.name = name;
        this.hp = hp;
        this.speed = speed;
        this.attack = attack;
        this.defense = defense;
        this.type = type;
        this.attacks = attacks;
        this.maxHp = hp;
        this.imagePath = imagePath;
    }

    public int chooseAndDoAttack(Monster target, Attack attack) {

        if (this instanceof SoilMonster) {
            ((SoilMonster)this).updateUndergroundStatus();
        }

        checkStatusHealing();

        applyStatusDamage();
        applyNatureHealing();

        if(handlePreAttackBlocking(attack, target)) {

            if (this.state == MonsterState.PARALYZED) {
                this.turnsParalyzed++;
                System.out.println(this.name + " est paralysé et n'arrive pas à bouger !");
            }
            return 0;
        }

        int dmg = doAttack(target,attack);

        if (this.state == MonsterState.PARALYZED) {
            this.turnsParalyzed++;
        }

        return dmg;
    }


    public void checkStatusHealing() {
        if (this.state == MonsterState.PARALYZED) {
            double healChance = (double) this.turnsParalyzed / 6.0;

            if (Math.random() < healChance) {
                this.state = MonsterState.NORMAL;
                this.turnsParalyzed = 0;
                System.out.println(this.name + " s'est débarrassé de la paralysie !");
            }
        }

        if ((this.state == MonsterState.BURNED || this.state == MonsterState.POISONED) && Monster.isFlooded) {
            this.state = MonsterState.NORMAL;
            System.out.println(this.name + " est soigné du statut (" + this.state + ") grâce au terrain inondé !");
        }
    }

    public void applyStatusDamage() {
        if (this.state == MonsterState.BURNED || this.state == MonsterState.POISONED) {
            int dmg = (int) Math.round(this.attack / 10.0);
            this.decreaseHp(dmg);
            System.out.println(this.name + " subit " + dmg + " dégâts de statut (" + this.state + ") !");
        }
    }

    public void applyNatureHealing() {
        if (this.type instanceof NatureType && Monster.isFlooded) {
            int healAmount = (int) Math.round(this.maxHp / 20.0);

            this.hp += healAmount;
            if (this.hp > this.maxHp) {
                this.hp = this.maxHp;
            }
            System.out.println(this.name + " (Nature) se soigne de " + healAmount + " PV grâce au terrain inondé. PV actuels: " + this.hp);
        }
    }

    protected boolean handlePreAttackBlocking(Attack attack, Monster target) {

        if (this.state == MonsterState.PARALYZED) {
            if (Math.random() <= 0.25) {
                return true;
            }
        }

        if(isFlooded && !(this.type instanceof WaterType)) {
            if(Monster.floodSource != null && Math.random() <= floodSource.getFallChance()) {
                int recoilDmg = (int)Math.round(this.attack / 4.0);
                this.decreaseHp(recoilDmg);
                System.out.println(this.name + " glisse sur le terrain inondé ! Subit " + recoilDmg + " dégâts.");
                return true;
            }
        }

        return false;
    }

    public static void setFlooded(boolean state, WaterMonster source, int turns) {
        isFlooded = state;
        if (state) {
            turnsFloodedLeft = turns;
            floodSource = source;
        } else {
            turnsFloodedLeft = 0;
            floodSource = null;
            System.out.println("L'inondation se retire du terrain.");
        }
    }

    public void heal(int amount) {
        this.hp += amount;
        if (this.hp > this.maxHp) {
            this.hp = this.maxHp;
        }
    }

    public void cureStatus(MonsterState statusToCure) {
        if (this.state == statusToCure) {
            this.state = MonsterState.NORMAL;
            if (statusToCure == MonsterState.PARALYZED) {
                this.turnsParalyzed = 0;
            }
            System.out.println(this.name + " est guéri du statut " + statusToCure + ".");
        } else {
            System.out.println(this.name + " n'est pas affecté par " + statusToCure + ".");
        }
    }

    public abstract int doAttack(Monster target, Attack attack);

    public void decreaseHp(int dmg) {
        this.hp -= dmg;
    }

    public static boolean isFlooded() { return isFlooded; }
    public static int getTurnsFloodedLeft() { return turnsFloodedLeft; }
    public static void updateFloodStatus() {
        if (isFlooded) {
            turnsFloodedLeft--;
            if (turnsFloodedLeft <= 0) {
                setFlooded(false, null, 0);
            }
        }
    }

    public ArrayList<Attack>  getAttacks() {
        return this.attacks;
    }
    public int getHp() {
        return this.hp;
    }
    public int getMaxHp() { return this.maxHp;}
    public String getName() {
        return this.name;
    }
    public MonsterState getState() {
        return this.state;
    }
    public Type getType() {
        return this.type;
    }
    public int getAttack() {
        return this.attack;
    }
    public int getDefense() {
        return this.defense;
    }
    public void setState(MonsterState monsterState) {
        this.state = monsterState;
    }

    public void setAttack(int newAttack) {
        this.attack = newAttack;
    }

    public void setDefense(int newDefense) {
        this.defense = defense;
    }

    public int getSpeed() {
        return this.speed;
    }

    public String getImagePath() {
        return this.imagePath;
    }
}