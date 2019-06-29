
import utils from '../util/index.js'

let dateFormat = (value) => {
  return utils.dateFormat(value,'YYYY-MM-DD');
}
let datetimeFormat = (value) => {
  return utils.dateFormat(value,'YYYY-MM-DD HH:mm:ss');
}
let moneyFormat = value => {
  return utils.moneyFormat(value);
}
export { dateFormat,datetimeFormat,moneyFormat }
