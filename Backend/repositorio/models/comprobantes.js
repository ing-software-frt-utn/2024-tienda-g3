'use strict';
const {
  Model
} = require('sequelize');
module.exports = (sequelize, DataTypes) => {
  class Comprobantes extends Model {
    /**
     * Helper method for defining associations.
     * This method is not a part of Sequelize lifecycle.
     * The `models/index` file will call this method automatically.
     */
    static associate(models){}

  }
  Comprobantes.init({id: {
    type: DataTypes.INTEGER,
    primaryKey: true,
    autoIncrement: true
  },
  cae: {
    type: DataTypes.STRING
  },
  numero: {
    type: DataTypes.STRING
  },
  estado: {
    type: DataTypes.STRING
  },
  tipoId: {
    type: DataTypes.INTEGER,
    references: {
      model: 'TipoComprobantes',
      key: 'id'
    }
  },
}, {
  sequelize,
  modelName: 'Comprobantes',
});

  return Comprobantes;
};