using { Currency, managed, cuid} from '@sap/cds/common';
using { my.bookshop as my } from '../db/schema';

service ExtendedService {

    /**
     * Reads the session context variable "myVariable" into the field MyVariable.
     */
    view SessionContext as select SESSION_CONTEXT('myVariable') as MyVariable : String(50) from my.Authors;
}
